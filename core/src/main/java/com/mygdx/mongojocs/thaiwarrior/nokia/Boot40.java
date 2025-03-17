package com.mygdx.mongojocs.thaiwarrior.nokia;


/*
 * Created on 03-nov-2004
 *
 */

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.FullCanvas;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.thaiwarrior.common.Game;
import com.mygdx.mongojocs.thaiwarrior.genmidp.Cheats;
import com.mygdx.mongojocs.thaiwarrior.genmidp.NoAudio;
import com.mygdx.mongojocs.thaiwarrior.genmidp.StdGame;

/**
 * @author Administrador
 *  
 */
public class Boot40 extends MIDlet implements Runnable {

    Thread thread;

    NokiaCanvas canvas;

    Game game;
    
    long targetMillis = 80;

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.midlet.MIDlet#startApp()
     */
    public void startApp()  {
        try {
            canvas = new NokiaCanvas();
            game = new StdGame(new NoAudio());
            game.displayWidth = canvas.getWidth();
            game.displayHeight = canvas.getHeight();
            game.customSoftKeyHandling = true;
            StdGame.canvas = canvas;
            //if (Game.CHEATS) {
                new Cheats(Display.getDisplay(this));
                Cheats.lastDisplayable = canvas;
            //}
            Display.getDisplay(this).setCurrent(canvas);
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.midlet.MIDlet#pauseApp()
     */
    protected void pauseApp() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
     */
    protected void destroyApp(boolean arg0) {
        System.out.println("Destroyed!");
        thread = null;
        Display.getDisplay(this).setCurrent(null);
        System.gc();
        notifyDestroyed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        System.gc();
        long startTime;
        long deltaTime;        
        while (!Game.finished && thread != null) {
            startTime =System.currentTimeMillis();
            game.updateLogic();
            if (!Game.finished) {
                canvas.repaint(0,0,canvas.getWidth(),canvas.getHeight());
                canvas.serviceRepaints();
                try {
                    deltaTime = System.currentTimeMillis() - startTime;
                    if (deltaTime < targetMillis) {
                        deltaTime = targetMillis - deltaTime;
                    } else {
                        deltaTime = 1;
                    }
                    Thread.sleep(deltaTime);
                } catch (Exception e) {

                }
            } else {
                try {
                    destroyApp(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 
     * @author Administrador
     *  
     */
    class NokiaCanvas extends FullCanvas {

        boolean allowKeyPressed = true;        

        /**
         * 
         *  
         */
        public NokiaCanvas() {
            super();            
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
         */
        public synchronized void paint(Graphics arg0) {
            StdGame.graphics = arg0;
            game.updateDisplay();            
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Displayable#keyReleased(int)
         */
        public void keyReleased(int arg0) {
            allowKeyPressed = true; 
            game.keyPressed = false;
            game.keyCode = Game.KEY_NONE;
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Displayable#keyPressed(int)
         */
        public synchronized void keyPressed(int arg0) {
            if (allowKeyPressed) {
                game.keyPressed = true;
                game.keyCode = Game.KEY_NONE;
                switch (getGameAction(arg0)) {
                    case Canvas.FIRE:
                        game.keyCode = Game.KEY_FIRE;
                        break;
                    case Canvas.UP:
                        game.keyCode = Game.KEY_UP;
                        break;
                    case Canvas.DOWN:
                        game.keyCode = Game.KEY_DOWN;
                        break;
                    case Canvas.LEFT:
                        game.keyCode = Game.KEY_LEFT;
                        break;
                    case Canvas.RIGHT:
                        game.keyCode = Game.KEY_RIGHT;
                        break;
                    case Canvas.GAME_A:
                        game.keyCode = Game.KEY_ACTION_A;
                        break;
                    case Canvas.GAME_B:
                        game.keyCode = Game.KEY_ACTION_B;
                        break;
                    case Canvas.GAME_C:
                        game.keyCode = Game.KEY_ACTION_C;
                        break;
                    case Canvas.GAME_D:
                        game.keyCode = Game.KEY_ACTION_D;
                        break;
                    default:
                        switch (arg0) {
                            case -6:
                                game.keyCode = Game.KEY_SOFT_1;
                                break;
                            case -7:
                                game.keyCode = Game.KEY_SOFT_2;
                                break;     
                            case 49:
                                game.keyCode = Game.KEY_UPLEFT;
                                break;
                            case 51:
                                game.keyCode = Game.KEY_UPRIGHT;
                                break;
                            case 42:
                                game.keyCode = Game.KEY_DOWNLEFT;
                                break;
                            case 35:
                                game.keyCode = Game.KEY_DOWNRIGHT;
                                break;
                        }
                }
                allowKeyPressed = false;
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Displayable#hideNotify()
         */
        protected void hideNotify() {
            game.pauseGame();
            super.hideNotify();
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Displayable#showNotify()
         */
        protected void showNotify() {
            game.resumeGame();
            super.showNotify();
        }
    }
}