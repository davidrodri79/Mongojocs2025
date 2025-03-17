package com.mygdx.mongojocs.iapplicationemu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.mongojocs.midletemu.Image;
import com.mygdx.mongojocs.midletemu.MIDlet;

public class MediaManager {

    static class ImageCreateBinaryTask implements Runnable {

        MediaImage image;
        byte buffer[];
        int start, length;
        boolean finished = false;

        ImageCreateBinaryTask(MediaImage i, byte[] b, int s, int l)
        {
            image = i;
            buffer = b;
            start = s; length = l;
        }

        @Override
        public void run() {
            try {

                Pixmap pixMap = new Pixmap(buffer, start, length);
                image.texture = new Texture(pixMap);

            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finished = true;
        }
    };

    public static MediaImage getImage(String s) {

        String resLoc = null;

        if(s.startsWith("resource://"))
        {
            resLoc = IApplication.assetsFolder+"/"+s.substring(12);
        }

        MediaImage im = new MediaImage();
        im.texture = new Texture( resLoc );
        return im;
    }

    public static MediaImage getImage(byte[] bytes) {

        MediaImage im = new MediaImage();

        ImageCreateBinaryTask task = new ImageCreateBinaryTask(im, bytes, 0, bytes.length);
        Gdx.app.postRunnable(task);
        while(!task.finished)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return im;
    }

    public static MediaSound getSound(String s) {
        return null;
    }


}
