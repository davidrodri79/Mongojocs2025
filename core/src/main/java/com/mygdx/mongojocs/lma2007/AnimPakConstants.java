package com.mygdx.mongojocs.lma2007;

/**
 * Constantes de empaquetado para el fichero 'pak'.
 * 
 * @author Tony
 *
 */
public interface AnimPakConstants {

    /**
     * Banco
     */
    public static final byte CHUNK_BANK = 0x00;
	/**
	 * Grupos
	 */
	public static final byte CHUNK_GROUPS = 0x01;
	/**
	 * PNG (cuando va incluido en el pak)
	 */
	public static final byte CHUNK_PNG = 0x02;
	/**
	 * Paletas
	 */
	public static final byte CHUNK_PALETTE = 0x03;
	/**
	 * Rect�ngulos de celdas
	 */
	public static final byte CHUNK_CELL_RECTS = 0x04;
	/**
	 * Secuencias
	 */
	public static final byte CHUNK_CELL_SEQUENCES = 0x05;
	/**
	 * Skins
	 */
	public static final byte CHUNK_SKINS = 0x06;
    
    /**
     * Tags
     */
    public static final byte CHUNK_TAGS = 0x07;
    
	/**
	 * Fin de fichero
	 */
	public static final byte CHUNK_EOF = (byte) 0xFF;
}
