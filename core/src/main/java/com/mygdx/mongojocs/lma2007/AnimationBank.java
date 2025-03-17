package com.mygdx.mongojocs.lma2007;
//#ifdef REM_2DMATCH
//#undef BUILD_ANIMATOR
//#endif

//#define NOHASHTABLES

//#ifdef BUILD_ANIMATOR


//#ifdef J2ME
//#elifdef DOJA
//#endif
	import com.mygdx.mongojocs.midletemu.Image;

    import java.io.*;
	import java.util.*;


/**
 * com.mygdx.mongojocs.lma2007.AnimationBank representa un banco de animaciones, el cual contiene la
 * informaci�n de secuencias, fotogramas e im�gen.
 * 
 * @author Tony
 * 
 */
public class AnimationBank implements AnimPakConstants {
    
    // cache de im�genes
	//#ifndef NOHASHTABLES
    private Hashtable BITMAP_CACHE = new Hashtable();
    private Vector BANKS = new Vector();
    //#endif
    
    // im�genes contenidas en este banco
//#ifdef J2ME
    Image[] bitmap;
//#elifdef DOJA
//#endif

    // informaci�n de recorte de celdas sobre las im�genes
    short[][][] rects;

    // n� de grupos de im�gen
    int cellGroups;

    // ancho de celda original
    int cellWidth;

    // alto de celda original
    int cellHeight;

    // correspondencia de �ndices de celda con grupos
    short[][] cellIndex;

    // informaci�n de fotogramas
    short[][][] frames;

    // informaci�n de capas
    short[][][][] layers;
    
    // paletas
    int[][] palette;
    
    char[] paletteTransparentIndex;
    
    // skins
    char[][] skinOffset;
    char[][] skinPalette;    
    char selectedSkin;

    // stream de entrada para lectura de datos
    DataInputStream dis;
    
    // identificador del banco
    int bankId;
    
    // identificador del conjunto de celdas
    int cellSetId;
    
    // puntos de referencia
    char[][][] tags;
    
    // cambiar esto o quitarlo, es temporal
    public static String DATA_DIR = "/";
    
    public static byte inputPalette[][];
    public static int outputPalette[];

    /**
     * Constructor de la clase
     * 
     * @param bankId
     *            identificador del banco
     * @param cellSetId
     *            identificador del conjunto de celdas
     * @param skinId
     * 			  identificador del skin a aplicar
     * @throws Exception
     */
    public AnimationBank(int bankId, int cellSetId, int skin) throws Exception {
    	    	
   	//#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Creando banco: " + bankId +" CS: " + cellSetId);
    //#endif
        this.bankId = bankId;
        this.cellSetId = cellSetId;
        this.selectedSkin = (char)skin;
        loadPak();
        //#ifndef NOHASHTABLES
        BANKS.addElement(this);
        //#endif
    }
    
    /**
     * Constructor de la clase
     * 
     * @param bankId
     *            identificador del banco
     * @param cellSetId
     *            identificador del conjunto de celdas
     * @throws Exception
     */
    public AnimationBank(int bankId, int cellSetId) throws Exception {
    	this(bankId,cellSetId,0);
    }
    
    /**
     * Libera recursos asociados a un banco, teniendo en consideraci�n que
     * no haya otros bancos utiliz�ndolos.
     * 
     * @param ab instancia del banco sobre el que operar.
     */
    public void releaseUnusedResources(AnimationBank ab) {
        /*String filename = DATA_DIR + "b" + ab.bankId + "c" + ab.cellSetId;
        boolean noMoreBanks = (BANKS.size() == 1);
        for (int c = 0; c < ab.cellGroups; c++) {
        	for (int a = 0; a < BANKS.size(); a++) {        		
        		ab = (com.mygdx.mongojocs.lma2007.AnimationBank)BANKS.elementAt(a);
        	//#ifdef J2ME
	            String pngName = filename + "g" + c;

	            //#if DEBUG && DEBUG_ANIMATOR
		        	com.mygdx.mongojocs.lma2007.Debug.println("Comprobando si a�n est� en uso: " + pngName);
	        	//#endif            

	            if (noMoreBanks || !isAnimationBankUsingBitmap(ab,pngName))
	            {
            	//#if DEBUG && DEBUG_ANIMATOR
	            	com.mygdx.mongojocs.lma2007.Debug.println("Liberando recursos gr�ficos: " + pngName);
            	//#endif

	            	//#ifndef NOHASHTABLES
	            	BITMAP_CACHE.remove(pngName);
	            	//#endif
	            }
            //#elifdef DOJA
	            
	            for (int d =0; d < bitmap[c].length; d++) {
	                String gifName = filename + "g" + c + "i" + d;
                //#if DEBUG && DEBUG_ANIMATOR
	            	com.mygdx.mongojocs.lma2007.Debug.println("Comprobando si a�n est� en uso: " + gifName);
            	//#endif            
	                if (noMoreBanks || !isAnimationBankUsingBitmap(ab,gifName)) {
                	//#if DEBUG && DEBUG_ANIMATOR
	                	com.mygdx.mongojocs.lma2007.Debug.println("Liberando recursos gr�ficos: " + gifName);
	               	//#endif
	                	BITMAP_CACHE.remove(gifName);                	
	                }
	            }
            //#endif
        	}            
        }
        BANKS.removeElement(ab);
        System.gc();*/
    }
    
    /**
     * Comprueba si un banco est� haciendo uso de un bitmap.
     * @param ab instancia del banco
     * @param filename nombre de fichero (sin extensi�n)
     * @return true si el banco est� usando dicho bitmap, false en caso contrario
     */
    public boolean isAnimationBankUsingBitmap(AnimationBank ab, String filename) {    	
    	String abfile = DATA_DIR + "b" + ab.bankId + "c" + ab.cellSetId;
    	boolean used = false;
    	for (int c = 0; c < ab.cellGroups && !used; c++) {
		//#ifdef J2ME
             String pngName = abfile + "g" + c;
             used = pngName.compareTo(filename) == 0;
        //#elifdef DOJA           
             /*for (int d =0; d < bitmap[c].length && !used; d++) {
                 String gifName = abfile + "g" + c + "i" + d;
                 used = gifName.compareTo(filename) == 0;
             }*/
        //#endif
        }
    	return used;
    }

    /*
     * Bucle principal de proceso de un fichero pak.
     */
    private void loadPak() throws Exception {
        boolean extBitmap = true;        
    //#ifdef J2ME        
        String filename = DATA_DIR + "b" + bankId + "c" + cellSetId;
    	//dis = new DataInputStream(getClass().getResourceAsStream(filename + ".pak"));
        //MONGOFIX
        byte bytes[] = GameCanvas.gc.loadFile(filename + ".pak");
        dis = new DataInputStream( new ByteArrayInputStream(bytes, 0, bytes.length));
	//#elifdef DOJA
   	//#endif
        byte chunkId = CHUNK_EOF;
        do {
            chunkId = dis.readByte();
        //#if DEBUG && DEBUG_ANIMATOR
            Debug.println("CHUNK ID: " + chunkId);
        //#endif
            switch (chunkId) {
                case CHUNK_BANK:
                    processBankChunk();
                    break;
                case CHUNK_GROUPS:
                    processGroupChunk();
                    break;
                //#ifdef J2ME
                case CHUNK_PNG:
                    extBitmap = false;
                    processPngChunk(filename);
                    break;
                //#endif
                case CHUNK_PALETTE:
                    processPaletteChunk();
                    break;
                case CHUNK_CELL_RECTS:
                    processCellRectsChunk();
                    break;
                case CHUNK_CELL_SEQUENCES:
                    processCellSequencesChunk();
                    break;
                case CHUNK_SKINS:
                    processSkinsChunk();
                    break;
                case CHUNK_TAGS:
                    processTagsChunk();
                    break;
            }
        } while (chunkId != CHUNK_EOF);
        dis.close();
        dis = null;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("PAK read");
    //#endif        
        if (extBitmap) {        	
            loadExtBitmap(filename);
        }
        System.gc();
    }
    
    /*
     * Carga gr�ficos que se encuentran fuera del pak.
     */
    private void loadExtBitmap(String filename) throws Exception {
    	//selectedSkin=1;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Cargando pngs para: " + filename+" Skin: " + (int)selectedSkin);
    //#endif                	    	
    	for (int c = 0; c < cellGroups; c++) {

      	//#ifdef J2ME
            String pngName = filename + "g" + c;
            String pngSkinName = pngName+"p"+(int)skinPalette[selectedSkin][c];

	        //#if DEBUG && DEBUG_ANIMATOR                
	            Debug.println("Obteniendo recurso: " + pngName +" // " + pngSkinName);
	        //#endif

            //#ifndef NOHASHTABLES    
            if (BITMAP_CACHE.containsKey(pngSkinName)) {
            //#if DEBUG && DEBUG_ANIMATOR                
                Debug.println("Recurso desde cache: " + pngName);
            //#endif                	
                bitmap[c] = (Image)BITMAP_CACHE.get(pngSkinName);
            //#else
            //#endif
            } else {
            //#if DEBUG && DEBUG_ANIMATOR                
                Debug.println("Recurso desde fichero: " + pngName);
            //#endif               
                if (selectedSkin !=0) {
                	int palIdx = skinPalette[selectedSkin][c];

				//#if DEBUG && DEBUG_ANIMATOR                
                    Debug.println("�ndice de paleta para skin: " + palIdx);
				//#endif  

                	bitmap[c] = changePal(GameCanvas.gc.loadFile(pngName+".png"),palette[palIdx]);
                } else {
                	
                	// bitmap[c]=com.mygdx.mongojocs.lma2007.GameCanvas.gc.loadImage(pngName);
                	
                	if(inputPalette != null) {
                	
                		bitmap[c] = GameCanvas.gc.changePal(GameCanvas.gc.loadFile(pngName+".png"), inputPalette, outputPalette);
                	
                	} else {
                		
                		bitmap[c] = GameCanvas.gc.loadImage(pngName);
                	}
                	
                }

                //#ifndef NOHASHTABLES
                BITMAP_CACHE.put(pngSkinName,bitmap[c]);
                //#endif
            }

        //#elifdef DOJA
        //#endif
        }
    }
    
    /*
     * Procesa los tags encontrados en el pak.
     */
    private void processTagsChunk() throws Exception {
        int tagIdCount = dis.readShort()&0xFFFF;
        int tagCount = dis.readShort()&0xFFFF;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando tags. Id count: " + tagIdCount+" Num Tags:" + tagCount);
    //#endif
        tags = new char[tagIdCount][tagCount][2];
        for (int t = 0;t < tagIdCount; t++) {
            for (int c = 0; c < tagCount; c++) {
                tags[t][c][0] = dis.readChar(); // tag x
                tags[t][c][1] = dis.readChar(); // tag y
            }
        }
    }
    
//#ifdef J2ME
    /*
     * Procesa ficheros PNG encontrados en el pak. Ojo, esto no se usa para
     * gifs ya que usan el IHD que ya los empaqueta.
     */
    private void processPngChunk(String filename) throws Exception {
    	for (int c = 0; c < cellGroups; c++) {
    		int bitmapDataLength = dis.readInt();
    		String pngName = filename + "g" + c;
    		String pngSkinName = pngName+"p"+(int)skinPalette[selectedSkin][c];
            //#ifndef NOHASHTABLES
    		if (BITMAP_CACHE.containsKey(pngSkinName)) {    		
            //#if DEBUG && DEBUG_ANIMATOR
                Debug.println("Recurso desde cache: " + pngName);
            //#endif                	
                dis.skip(bitmapDataLength);
                bitmap[c] = (Image)BITMAP_CACHE.get(pngSkinName);
        	//#else
            //#endif    
            } else {
            //#if DEBUG && DEBUG_ANIMATOR                
                Debug.println("Recurso desde fichero: " + pngName);
            //#endif    
                byte[] data = new byte[bitmapDataLength];
        		dis.readFully(data);    		        		
                if (selectedSkin !=0) {
                	int palIdx = skinPalette[selectedSkin][c];
				//#if DEBUG && DEBUG_ANIMATOR                
                    Debug.println("�ndice de paleta para skin: " + palIdx);
				//#endif  
                	bitmap[c] = changePal(data,palette[palIdx]);
                } else {
                	bitmap[c]= Image.createImage(data,0,data.length);
                }
                //#ifndef NOHASHTABLES
                BITMAP_CACHE.put(pngSkinName,bitmap[c]);
                //#endif
            }
    	}
    }
 //#endif
    
    /*
     * Procesa informaci�n de skins encontrada en el pak.
     */
    private void processSkinsChunk() throws Exception {
   	//#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando skins.");
    //#endif
        short numSkins = dis.readShort();
        char numLayers = 0;
        char numPalettes = 0;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("NumSkins: " + numSkins);
    //#endif        
        skinOffset = new char[numSkins][];
        skinPalette = new char[numSkins][];
        for (int c =0; c < numSkins; c++) {
            numLayers = dis.readChar();
        //#if DEBUG && DEBUG_ANIMATOR
            Debug.println("Skin: " + c + " NumLayers: " + (int)numLayers);
        //#endif            
            skinOffset[c] = new char[numLayers];
            for (int l = 0; l < numLayers; l++) {
                skinOffset[c][l] = dis.readChar();                                                          
            }
            numPalettes = dis.readChar();
        //#if DEBUG && DEBUG_ANIMATOR
            Debug.println("Skin: " + c + " NumPalettes: " + (int)numPalettes);
        //#endif                        
            skinPalette[c] = new char[numPalettes];
            for (int p = 0; p < numPalettes; p++) {
            	skinPalette[c][p] = dis.readChar();
            }
        }
        
    }
    
    /*
     * Procesa secuencias de animaci�n encontradas en el pak.
     */
    private void processCellSequencesChunk() throws Exception {
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando secuencias.");
    //#endif        
        int numSequences = dis.readShort() & 0xFFFF;
        frames = new short[numSequences][][];
        layers = new short[numSequences][][][];
        for (int c = 0; c < numSequences; c++) {
            int numFrames = dis.readShort() & 0xFFFF;
            frames[c] = new short[numFrames][4];
            layers[c] = new short[numFrames][][];
            for (int f = 0; f < numFrames; f++) {
                frames[c][f][0] = dis.readShort(); // frame x
                frames[c][f][1] = dis.readShort(); // frame y
                frames[c][f][2] = dis.readShort(); // frame width
                frames[c][f][3] = dis.readShort(); // frame height
                int numLayers = dis.readByte() & 0xFF;
                layers[c][f] = new short[numLayers][4];
                for (int l = 0; l < numLayers; l++) {
                    for (int d = 0; d < 3; d++) { // cell index, x, y
                        layers[c][f][l][d] = dis.readShort(); 
                    }
                    for (int b = 1; b <= 2; b++) { // flip x, y
                        if (dis.readBoolean()) { 
                            layers[c][f][l][3] += b;
                        }
                    }
                }
            }
        }        
    }

    /*
     * Procesa los rects asociados a las celdas encontrados en el pak.
     */
    private void processCellRectsChunk() throws Exception {
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando rects.");
    //#endif
        int group;
        int numCells;
        group = dis.readByte() & 0xFF;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Grupo: " + group);
    //#endif
        numCells = dis.readShort() & 0xFFFF;
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("NumCells: " + numCells);
    //#endif       
        rects[group] = new short[numCells][6];
        for (int c = 0; c < numCells; c++) {
            short ci = dis.readShort(); // grupo e �ndice de celda            
            cellIndex[ci][0] = (short) group;
            cellIndex[ci][1] = (short) c;
            for (int d = 0; d < 6; d++) { // gap x, gap y, x, y, ancho, alto                
                rects[group][c][d] = dis.readShort();
            }
        }
    //#ifdef DOJA
    //#endif
    }

    /*
     * Procesa la informaci�n de grupos encontrada en el pak.
     */
    private void processGroupChunk() throws Exception {
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando grupos.");
    //#endif

        cellGroups = dis.readByte() & 0xFF;
    //#ifdef J2ME
        bitmap = new Image[cellGroups];
    //#elifdef DOJA
    //#endif
        rects = new short[cellGroups][][];
    }

    /*
     * Procesa la informaci�n de banco encontrada en el pak.
     */
    private void processBankChunk() throws Exception {
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando banco.");
    //#endif
        int cellCount;
        cellWidth = dis.readShort() & 0xFFFF;
        cellHeight = dis.readShort() & 0xFFFF;
        cellCount = dis.readShort() & 0xFFFF;
        cellIndex = new short[cellCount][2];
    //#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Num.Cells: "+cellCount+" Cell Width: " + cellWidth+" Cell Height: " + cellHeight);
    //#endif
    }
    
    /*
     * Procesa la informaci�n de paletas encontrada en el pak.
     */
    private void processPaletteChunk() throws Exception {    
        char numPalettes = dis.readChar();
  	//#if DEBUG && DEBUG_ANIMATOR
        Debug.println("Procesando paletas: " + (int)numPalettes);
    //#endif
        palette = new int[numPalettes][];
        paletteTransparentIndex = new char[numPalettes];
        for (int p=0; p < numPalettes; p++) {
            paletteTransparentIndex[p] = dis.readChar();
            palette[p] = new int[dis.readChar()];
            for (int e = 0; e < palette[p].length; e++) {
                palette[p][e] = dis.readInt();
            }
        }
        rgbs = new byte[palette[0].length][3];
  	//#if DEBUG && DEBUG_ANIMATOR
        Debug.println("RGBS Length: " + rgbs.length);
    //#endif
        for (int c = 0; c < palette[0].length; c++) {
        	rgbs[c][0] = (byte)((palette[0][c]>>16)&0xFF);
        	rgbs[c][1] = (byte)((palette[0][c]>>8)&0xFF);
        	rgbs[c][2] = (byte)(palette[0][c]&0xFF);
        }
//paletas:
rgbs = null;
paletteTransparentIndex = null;
palette = null;

    }

    
    //--------------------------------------------------------------------------
    // Cambio de paleta png/gif en tiempo de carga
    //--------------------------------------------------------------------------
    byte rgbs[][];
    
//#ifdef J2ME
	public Image changePal(byte inbuf[], int shirtCol[])
	{
		int i = 0x20;
		while (true)
		{
			if (inbuf[i] == 80 && inbuf[i+1] == 76 && inbuf[i+2] == 84 && inbuf[i+3] == 69) break;
			i++;
		}

	//#if DEBUG && DEBUG_ANIMATOR
		Debug.println("CHANGEPAL: PLTE IDX -> " + i);
	//#endif

		int b = i;
		i += 4;          
		int length = (inbuf[i-5]&0xFF)+((inbuf[i-6]&0xFF)<<8);
		
	//#if DEBUG && DEBUG_ANIMATOR
		Debug.println("CHANGEPAL: PAL LENGTH -> " + length);
	//#endif
		
		for(int j = 0; j < length; j+=3)
		{
			boolean found = false;
			int k = 0;                              
			while (!found && k < shirtCol.length)
			{
				if (((inbuf[i+j]&0xf0) == (rgbs[k][0]&0xf0)) && ((inbuf[i+j+1]&0xf0) == (rgbs[k][1]&0xf0)) && ((inbuf[i+j+2]&0xf0) == (rgbs[k][2]&0xf0)))
				{
				//#if DEBUG && DEBUG_ANIMATOR
					Debug.println("CHANGEPAL: PAL ENTRY FOUND AT: -> " + k+","+(i+j));
				//#endif

					inbuf[i+j] = (byte)((shirtCol[k]>>16)& 0x000000ff);
					inbuf[i+j+1] = (byte)((shirtCol[k]>>8)& 0x000000ff);
					inbuf[i+j+2] = (byte)(shirtCol[k] &0x000000ff);

					found = true;
				}
				k++;
			}
		}

		i += length;

		int crc = crc(inbuf, b, length + 4);
		inbuf[i] = (byte)((crc&0xff000000)>>24);
		inbuf[i+1] = (byte)((crc&0x00ff0000)>>16);
		inbuf[i+2] = (byte)((crc&0x0000ff00)>>8);
		inbuf[i+3] = (byte)(crc&0x000000ff);

		return  Image.createImage(inbuf,0,inbuf.length);
	}




	public int update_crc(int crc, byte buf[], int off, int len)
	{
		int c = crc;
		int n = off;
		if (!crc_table_computed) make_crc_table();

		for (n = 0; n < len; n++)
		{
			c = crc_table[(c ^ buf[off+n]) & 0xff] ^ (c >>> 8);
		}

		return c;
	}


/* Return the CRC of the bytes buf[0..len-1]. */
	public int crc(byte buf[], int off, int len)
	{
		return update_crc(0xffffffff, buf, off, len) ^ 0xffffffff;
	}


	int crc_table[] = new int[256];
	boolean crc_table_computed = false;

/* Make the table for a fast CRC. */
	public void make_crc_table()
	{
		int c;
		int n, k;
		for (n = 0; n < 256; n++)
		{
			c = n;
			for (k = 0; k < 8; k++)
			{
				if ((c & 1) == 1) c = 0xedb88320 ^ (c >>> 1); else c = c >>> 1;
			}
			crc_table[n] = c;
		}
		crc_table_computed = true;
	}

//#elifdef DOJA
//#endif
	
	public void freeAll() {
		
         //#ifndef NOHASHTABLES
		 BITMAP_CACHE = new Hashtable();		 
		 BANKS = new Vector();
		 //#endif
		 
		 System.gc();
	}

}

//#endif