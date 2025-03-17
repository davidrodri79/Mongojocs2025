package com.mygdx.mongojocs.thaiwarrior.genmidp;

/*
 * Created on 03-nov-2004
 *
 */

import com.mygdx.mongojocs.midletemu.Canvas;
import com.mygdx.mongojocs.midletemu.Command;
import com.mygdx.mongojocs.midletemu.CommandListener;
import com.mygdx.mongojocs.midletemu.Display;
import com.mygdx.mongojocs.midletemu.Displayable;
import com.mygdx.mongojocs.midletemu.Graphics;
import com.mygdx.mongojocs.midletemu.MIDlet;
import com.mygdx.mongojocs.thaiwarrior.common.Game;

/**
 * @author Administrador
 *  
 */
public class BootMidp1MmaAudio extends MIDlet implements Runnable {

    Thread thread;

    MidpCanvas canvas;

    Game game;
    
    long targetMillis = 80;

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.midlet.MIDlet#startApp()
     */
    public void startApp() {
        try {
            canvas = new MidpCanvas();
            game = new StdGame(new AudioMma());
            game.displayWidth = canvas.getWidth();
            game.displayHeight = canvas.getHeight();
            StdGame.canvas = canvas;
           // if (Game.CHEATS) {
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

    long fps;
    long logicTime;
    long paintTime;
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        System.gc();
        long startTime;
        long deltaTime;        
        long fpsTimeStart = System.currentTimeMillis();
        long fpsCount =0;
        while (!Game.finished && thread != null) {
            startTime =System.currentTimeMillis();
            logicTime = System.currentTimeMillis();
            game.updateLogic();
            logicTime = System.currentTimeMillis() - logicTime;
            if (!Game.finished) {                
                canvas.repaint(0,0,game.displayWidth,game.displayHeight);
                canvas.serviceRepaints();                
                try {
                    deltaTime = System.currentTimeMillis() - startTime;
                    if (deltaTime < targetMillis) {                        
                        Thread.sleep(targetMillis - deltaTime);
                    } else {
                        Thread.sleep(1);
                    }
                } catch (Exception e) {

                }
            } else {
                try {
                    destroyApp(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
            fpsCount ++;
            if (System.currentTimeMillis() - fpsTimeStart >= 1000) {
                fps = fpsCount;
                fpsCount = 0;
                fpsTimeStart =System.currentTimeMillis();
            } 
        }
    }

    /**
     * 
     * @author Administrador
     *  
     */
    class MidpCanvas extends Canvas implements CommandListener {

        boolean allowKeyPressed = true;
        
        /**
         * 
         *  
         */
        public MidpCanvas() {
            super();
            setCommandListener(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
         */
        public synchronized void paint(Graphics arg0) {
            StdGame.graphics = arg0;
            //paintTime = System.currentTimeMillis();
            game.updateDisplay();        
            //paintTime = System.currentTimeMillis() - paintTime;
//            arg0.setClip(0,0,getWidth(),getHeight());
//            arg0.drawString("FPS: " + fps,getWidth()-8,4,Graphics.TOP|Graphics.RIGHT);
//            arg0.drawString("LT: " + logicTime,getWidth()-8,20,Graphics.TOP|Graphics.RIGHT);
//            arg0.drawString("PT: " + paintTime,getWidth()-8,36,Graphics.TOP|Graphics.RIGHT);
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
        
        
        /* (non-Javadoc)
         * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
         */
        public void commandAction(Command arg0, Displayable arg1) {
            game.keyPressed=true;
           if (arg0.getPriority() == 0) {
               game.keyCode = Game.KEY_SOFT_1;
           } else {
               game.keyCode = Game.KEY_SOFT_2;
           }
        }
    }
}