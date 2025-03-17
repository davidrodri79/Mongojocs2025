package com.mygdx.mongojocs.juegoverdad;

import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.Image;

import java.util.Vector;

class PreguntaGame {
    //final static int TIME = 10;

	public int scores;
	public Image sprites[];
	public int gameExit;
	public int dif;
	public String[][] word;
	public Vector playedWords=new Vector();
	public String disorderWord, orderWord, userWord= null;
	boolean introWord=false;
	boolean correct = false;
	int solvedTime, maxTime;

	public PreguntaGame(Game ga) {
		this.ga = ga;
		this.gc = ga.gc;
	}

	/**
	 *	Inicializa el mini juego del supermercado
	 *	Recibe por par�metros la puntuci�n inicial (si es necesario acumularla),
	 *	la dificultad 'relativa' donde el valor 1024 equivale a la velodidad 
	 *	'normal' y el tiempo para el timeout (0 indica tiempo infinito)
	 */
	public void playInit(int puntos, int dif, int time) {
//#ifdef DOJA
//#else 
		word = ga.textosCreate( ga.loadFile("/palabras.txt") );
//#endif
		int totalWords=word[3].length+word[4].length+
			       word[5].length+word[6].length+word[7].length;

		introWord=false;
		disorderWord=null;
		orderWord=null;
		userWord=null;
		maxTime=time;

		this.dif=dif;

		orderWord = chooseWord().toUpperCase();

		// estamos jugando varias partidas (standAlone)
		if (scores > 0) {
			while (playedWords.contains(orderWord)&&playedWords.size()<totalWords) {
				orderWord = chooseWord().toUpperCase();
			}
		}

		playedWords.addElement(orderWord);

		disorderWord = disorder(orderWord).toUpperCase();


		scores = puntos;
		timeOut = 1000 * time;


		canvasWidth = gc.canvasWidth;
		canvasHeight = gc.canvasWidth;

		canvasWidth = gc.getWidth();
		canvasHeight = gc.getHeight();

		gameExit = 0;

		//scores = 0;
		gc.xPressed = 0;
		gc.yPressed = 0;
		gc.fPressed = 0;

		gc.canvasFillInit(0);
		gc.canvasTextInit(ga.gameText[ga.TEXT_LOADING][0], 0, 0, 0xFFFFFF, gc.TEXT_VCENTER | gc.TEXT_HCENTER);
		gc.gameDraw();

		if (sprites == null) {
//#ifdef J2ME
			sprites = gc.loadImage("/5_preg", 1);
//#else
//#endif

			System.gc();
		}
		gc.miniGameScroll(0x197B30, 0x08D60F, sprites[0], " \\n"+ga.gameText[ga.TEXT_PREGUNTA_INSTRUC][0]);

		lastFrameTime = -1;
		currentGameTime = 0;

		ga.RNDSet();

//#if DOJA || LISTENER
//#else
		gc.softKey[1] = ga.gameText[ga.TEXT_ENTER][0];
//#endif

	}

	public void releaseGame() { sprites = null; word=null; ga.textBox=null; System.gc(); }

	public String chooseWord() {


        int i = this.dif/1024;

       	if (i>4) i=4;
       	if (i<1) i=1;

		// estamos jugando varias partidas standAlone
		if (scores>0) i=ga.RND(4)+1;
        	return word[i+2][ga.RND(word[i+2].length)];
	}


	//public boolean wordsLeft(Vector choosenWords) {
	//	return word[(this.dif/1024)+2].length>choosenWords.size();
	//}

	public String disorder(String str) {
		String aux=str;
		StringBuffer sb = new StringBuffer(str);
		int count = 5;
		while (aux.equals(str) && (count > 0)) {
			sb=new StringBuffer(str);
			sb.reverse();
			for (int i=0; i<str.length()/2; i++) {
				int a=ga.RND(str.length());
				int b=ga.RND(str.length());
				if (a == b) {
					a = (a + 1)%str.length();
				}
				char ch=sb.charAt(a);
				sb.setCharAt(a,sb.charAt(b));
				sb.setCharAt(b,ch);
				ga.RNDSet();
			}
			aux=sb.toString();
			count--;
		}
		if (aux.equals(str)) {
			return sb.reverse().toString().toUpperCase();
		}
		else {
			return aux.toUpperCase();
		}
	}


	/**
	 *	Devuelve true si es necesario salir del juego. this.gameExit valdr� el 
	 * c�digo de salida
	 */
	public boolean playTick() {
		long dT = ga.GameMilis - lastFrameTime;
		if (lastFrameTime == -1) {
			lastFrameTime = ga.GameMilis;
		} else {
			if(dT > 0 && dT < 1000) {
				currentGameTime += dT;
			}
		}

		lastFrameTime = ga.GameMilis;


		int xP = gc.xPressed;
		gc.xPressed -= xP;

//#if DOJA || LISTENER
//#else
		if (userWord!=null) { 
			gc.softKey[1] = null;
		}
//#endif

		gc.showBackground = true;

        if (gc.skPressed==1 && userWord==null) {
			introWord=true;
			gc.skPressed=0;
			solvedTime=(maxTime-(int)(currentGameTime/1000));
		}

		if ((maxTime-(int)(currentGameTime/1000)) < 0 && userWord==null)  {
			gameExit=3;
			return true;
		}

		if (userWord!=null) {
		    userWord = userWord.toLowerCase().replace('�', 'a').replace('�', 'e').replace('�', 'i').replace('�', 'o').replace('�', 'u');
			correct = userWord.equals(orderWord.toLowerCase());

		    if ((solvedTime - 2 > maxTime-(int)(currentGameTime/1000))) {
		    	if (correct) {
					scores += (45 - solvedTime/2)*(orderWord.length() - 2);
		    		gameExit=1;
				} else {
					gameExit=3;  
				}
				introWord=false;
				userWord=null;
				correct=false;
                return true;
            }
			System.gc();
		}

		return false;
	}

	public void playDraw() {
		scr = gc.scr;

//#ifdef J2ME
	scr.setClip(0, 0, canvasWidth, canvasHeight);
//#endif

		String strTime=Integer.toString((maxTime-(int)(currentGameTime/1000)));

		if (userWord==null) {
			gc.textDraw(strTime, 0, 2, 0xFFFFFF,GameCanvas.TEXT_HCENTER | GameCanvas.TEXT_TOP);
			gc.textDraw(disorderWord, 0, 0, 0xFFFFFF,GameCanvas.TEXT_HCENTER | GameCanvas.TEXT_VCENTER);

		} else {
			if (correct)
				gc.textDraw(ga.gameText[ga.TEXT_PREGUNTA_CORRECTO][0], 0, 0, 0xFFFFFF,GameCanvas.TEXT_HCENTER | GameCanvas.TEXT_VCENTER);
			else
				gc.textDraw(ga.gameText[ga.TEXT_PREGUNTA_INCORRECTO][0], 0, 0, 0xFFFFFF,GameCanvas.TEXT_HCENTER | GameCanvas.TEXT_VCENTER);
		}
	}


	private Game ga;
	private GameCanvas gc;
	private Graphics scr;

	private int canvasWidth;
	private int canvasHeight;


	// Variables espec�ficas de este juego

	private long timeOut = 0;

	private long lastFrameTime;
	private long currentGameTime;

}

