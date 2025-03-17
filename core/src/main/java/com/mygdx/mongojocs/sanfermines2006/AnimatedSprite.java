package com.mygdx.mongojocs.sanfermines2006;

//#ifdef BUILD_ANIMATOR


import com.badlogic.gdx.Gdx;
import com.mygdx.mongojocs.midletemu.Graphics;

/**
 * com.mygdx.mongojocs.sanfermines2006.AnimatedSprite representa un sprite con un banco de animaci�n asociado para
 * su representaci�n animada en pantalla.
 * 
 * @author Tony
 * 
 */
public class AnimatedSprite {

    // posici�n x al comenzar la animaci�n
    int sx;

    // posici�n y al comenzar la animaci�n
    int sy;

    // posici�n x 'actual'
    int x;

    // posici�n y 'actual'
    int y;

    // �ndice de secuencia asociado
    int sequence;

    // �ndice de fotograma actual
    int frameIndex;

    // �ndice del �ltimo fotograma
    int frameIndexMax;

    // flag para indicar que la secuencia de animaci�n ha terminado
    boolean animFinished;

    // banco de animaci�n asociado a este sprite
    AnimationBank bank;
   
    // flag de uso interno para se�alizar que ya se actualiz� la posici�n del
    // sprite asociada al fotograma actual.
    boolean locUpdated;
    
    boolean visible = true;

    /**
     * 
     *
     */
    public AnimatedSprite() {
    	
    }
    
    /**
     * Constructor de la clase.
     * 
     * @param bank
     *            banco de animaci�n a asociar.
     */
    public AnimatedSprite(AnimationBank bank) {
        setAnimationBank(bank);
    }

    /**
     * Asocia un nuevo banco de animaci�n a este sprite.
     * 
     * @param bank
     *            banco de animaci�n a asociar.
     */
    public void setAnimationBank(AnimationBank newBank) {
        bank = newBank;
        setSequence(0);
    }

    /**
     * Fija una secuencia de animaci�n de las contenidas en el banco.
     * 
     * @param seqIdx
     *            �ndice de la secuencia de animaci�n.
     */
    public void setSequence(int seqIdx) {
        animFinished = false;
        sx = x;
        sy = y;
        sequence = seqIdx;
        frameIndex = -1;
        frameIndexMax = bank.frames[seqIdx].length - 1;
        updateFrame();
//        updateLocation();
    }

    /**
     * Actualiza internamente el sprite avanzando por la lista de fotogramas.
     * 
     */
    public void updateFrame() {
    	
    	//#ifdef FAST150
    	//#else
    	
        if (frameIndex < frameIndexMax) {
            frameIndex++;
            locUpdated = false;
        } else {
            animFinished = true;
        }
        
        //#endif
    }

	/**
    * Actualiza internamente el sprite avanzando por la lista de fotogramas.
    * Repite la secuencia c�clicamente
    */
      public void updateFrameLoop() {
    	  
    	  //#ifdef FAST150
    	  //#else
    	  
    	  if (frameIndex < frameIndexMax) {
    		  frameIndex++;
    	  } else {
    		  setSequence(sequence);
    	  }
    	  locUpdated = false;
    	  
    	  //#endif
   } 

    /**
     * Actualiza internamente el sprite posicion�ndolo de acuerdo a las 
     * coordenadas asociadas al fotograma resultante.
     */
    public void updateLocation() {
    	if (!locUpdated) {
            	x = bank.frames[sequence][frameIndex][0] + sx;
        		y = bank.frames[sequence][frameIndex][1] + sy;
        	locUpdated = true;
    	}
    }
    
    /**
     * Obtiene el ancho del fotograma completo.
     * 
     * @return valor del primer punto de la l�nea base.
     */
    public int getFrameWidth() {
    	return bank.frames[sequence][frameIndex][2];
    }
        
    /**
     * Obtiene el alto del fotograma completo.
     * 
     * @return valor del punto final de la l�nea base.
     */
    public int getFrameHeight() {
    	return bank.frames[sequence][frameIndex][3];
    }
    
    /**
     * Obtiene la coordenada x de un tag.
     * @param tagId identificador del tag.
     * @param layer capa a inspeccionar en busca del tag.
     * @return valor de la coordenada x del tag.
     */
    public int getTagX(int tagId, int layer) {
    	return x+bank.layers[sequence][frameIndex][layer][1]+bank.tags[tagId][bank.layers[sequence][frameIndex][layer][0]][0];
    }

    /**
     * Obtiene la coordenada y de un tag.
     * @param tagId identificador del tag.
     * @param layer capa a inspeccionar en busca del tag.
     * @return valor de la coordenada y del tag.
     */
    public int getTagY(int tagId, int layer) {
    	return y+bank.layers[sequence][frameIndex][layer][2]+bank.tags[tagId][bank.layers[sequence][frameIndex][layer][0]][1];
    }
    

    /**
     * Dibuja en pantalla el sprite, considerando su estado actual de animaci�n.
     *
     * @param g
     *            contexto gr�fico donde dibujarlo.
     */
    public void paint(Graphics g) {
    	if (!visible) {
    		return;
    	}
	//#ifdef GOATSOMETRIC
     //   int tr[] = transf2iso(new int[] {x<<8,y<<8});
	//#endif
        int layerCount = bank.layers[sequence][frameIndex].length;        
        for (int c = 0; c < layerCount; c++) {
            int skcof =bank.selectedSkin>0?bank.skinOffset[bank.selectedSkin][c]:0;
            // �ndice de celda
            int ci = bank.layers[sequence][frameIndex][c][0]+skcof;
            //System.out.println("LY: "+c+ " CI: " + ci);
            // offset x de capa
            int xx = bank.layers[sequence][frameIndex][c][1];
            // offset y de capa
            int yy = bank.layers[sequence][frameIndex][c][2];
        //#if MIDP20 || NOKIAUI
            // flip x de capa
            boolean isFlipX = ((bank.layers[sequence][frameIndex][c][3] & 0x01) != 0) ;
            // flip y de capa
            boolean isFlipY = ((bank.layers[sequence][frameIndex][c][3] & 0x02) != 0);
        //#endif
            // grupo en el que se encuentra la celda
            int grp = bank.cellIndex[ci][0];
            // correspondencia de �ndice dentro del grupo
            int idx = bank.cellIndex[ci][1];
            // rect�ngulo que contiene la imagen de la celda
        //#ifdef J2ME
            int rx = bank.rects[grp][idx][2];
            int ry = bank.rects[grp][idx][3];
            int rw = bank.rects[grp][idx][4];
            int rh = bank.rects[grp][idx][5];
        //#endif            
            // aplicar c�lculos 
        //#ifdef GOATSOMETRIC
//juan            xx += (tr[0]>>8)/*+bank.rects[grp][idx][0]*/-com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.scrollX; //Si hacemos un scroll "emmarcado, habra que tenerlo en cuenta aqui.
//juan            yy += (tr[1]>>8)/*+bank.rects[grp][idx][1]*/-com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.scrollY;
            /*xx += (tr[0]>>8); //Si hacemos un scroll "emmarcado, habra que tenerlo en cuenta aqui.
            yy += (tr[1]>>8);
            xx -= getFrameWidth()/2;
            yy -= getFrameHeight();*/
            
        //#else

//juan            xx += x/*+bank.rects[grp][idx][0]*/-com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.scrollX;
//juan            yy += y/*+bank.rects[grp][idx][1]*/-com.mygdx.mongojocs.sanfermines2006.GameCanvas.gc.scrollY;
			//#ifdef BUILD_SCROLLLL
	         //#else
	            xx += x;
	            yy += y;
			//#endif

        //#endif
            
            // dibujar           
        //#ifdef J2ME
	            
           if(xx < GameCanvas.gc.canvasWidth && yy < GameCanvas.gc.canvasHeight && xx + rw > 0 && yy + rh > 0) {

	    	   g.setClip(0, 0, GameCanvas.gc.canvasWidth, GameCanvas.gc.canvasHeight);
	    	   g.clipRect(xx, yy, rw, rh);
	       

	        //#ifdef MIDP20                    
	         //   g.drawRegion(bank.bitmap[grp],rx,ry,rw,rh,(isFlipX?javax.microedition.lcdui.game.Sprite.TRANS_MIRROR:0)|(isFlipY?javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180:0),xx,yy,Graphics.TOP|Graphics.LEFT);
	        //#else
	            g.drawImage(bank.bitmap[grp], xx - rx, yy - ry,Graphics.TOP|Graphics.LEFT);
	        //#endif
           }
        //#elifdef DOJA
           //TONY VERA MANTA EXCEPTION
        //#endif            	       
        }
    }
        
//#ifdef GOATSOMETRIC
//#endif

}

//#endif
